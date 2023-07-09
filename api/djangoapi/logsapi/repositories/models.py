from django.db import models


class Upload(models.Model):
    filename = models.TextField(db_column="filename")
    bucket = models.TextField(db_column="bucket")
    size = models.PositiveBigIntegerField(db_column="size")
    processed = models.BooleanField(db_column="processed")
    uploadtime = models.DateField(db_column="uploadtime")
    tag = models.TextField(db_column="uuidtag")
    filetype = models.TextField(db_column="type")

    class Meta:
        db_table = "upload"
        app_label = "repositories"
