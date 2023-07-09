from django.conf import settings
from minio import Minio, S3Error
from minio.datatypes import Bucket
from logsapi.models.buckets import BucketModel


class MinioStorage:
    def __init__(self):
        self.host = settings.MINIO.get("HOST"),
        self.accesskey = settings.MINIO.get("ACCESSKEY")
        self.secretkey = settings.MINIO.get("SECRETKEY")
        self.client = Minio(self.host.__getitem__(0), access_key=self.accesskey, secret_key=self.secretkey, secure=False)

    def list_buckets(self):
        buckets: list[Bucket] = self.client.list_buckets()
        if len(buckets):
            bucket_m = [BucketModel(name=b.name).model_dump() for b in buckets]
            return bucket_m
        return []

    def create_bucket(self, bucket: BucketModel) -> (bool, str):
        try:
            self.client.make_bucket(bucket.name)
            return True, "created"
        except S3Error as e:
            return False, e.message