from django.conf import settings
from minio import Minio, S3Error
from minio.datatypes import Bucket
from logsapi.models.buckets import BucketModel
from django.core.files.uploadedfile import InMemoryUploadedFile

class MinioStorage:
    def __init__(self):
        self.host = settings.MINIO.get("HOST")
        self.accesskey = settings.MINIO.get("ACCESSKEY")
        self.secretkey = settings.MINIO.get("SECRETKEY")
        self.client = Minio(
            self.host,
            access_key=self.accesskey,
            secret_key=self.secretkey,
            secure=False,
        )

    def list_buckets(self):
        buckets: list[Bucket] = self.client.list_buckets()
        if len(buckets):
            bucket_m = [BucketModel(name=b.name).model_dump() for b in buckets]
            return bucket_m
        return []

    def create_bucket(self, bucket: BucketModel) -> tuple[bool, str]:
        try:
            self.client.make_bucket(bucket.name)
            return True, "created"
        except S3Error as e:
            return False, e.message

    def delete_bucket(self, bucket: BucketModel):
        find_bucket = self.client.bucket_exists(bucket.name)
        if find_bucket:
            self.client.remove_bucket(bucket.name)
            return True
        return False
    
    def upload_file(self, f: InMemoryUploadedFile, bucket: str):
        # chunk_size = 1024 * 1024 # 1MB
        part_size = 5 * 1024 * 1024 #5MB
        find_bucket = self.client.bucket_exists(bucket)
        if find_bucket:
            self.client.put_object(bucket_name=bucket, object_name=f.name, data=f, length=f.size, part_size=part_size) 
            return True
        return False  
    
    # def __read_chunks(self, f, chunk_size):
    #     while(chunk := f.read(chunk_size)):
    #         yield chunk