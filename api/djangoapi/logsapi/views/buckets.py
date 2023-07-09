from django.http import JsonResponse
from django.views import View
from django.views.decorators.http import require_http_methods

from logsapi.storage.miniostorage import MinioStorage


class Buckets(View):
    def __init__(self, **kwargs):
        super().__init__(**kwargs)
        self.minio = MinioStorage()

    @require_http_methods(["GET"])
    def get(self, request):
        buckets  = self.minio.list_buckets()
        return JsonResponse(buckets, safe=False)
