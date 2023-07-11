from django.urls import path, register_converter
from logsapi.views import buckets, files, bucketfiles
from logsapi.utils import Pathstrconverter

register_converter(Pathstrconverter, "bucketname")

urlpatterns = [
    path("buckets", buckets.Buckets.as_view(), name="buckets"),
    path("buckets/<bucketname:name>", bucketfiles.BucketFiles.as_view(), name="bucket_files"),
    path("files/<bucketname:name>", files.Files.as_view(), name="files")
]