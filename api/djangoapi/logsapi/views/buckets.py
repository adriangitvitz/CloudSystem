from django.http import JsonResponse
from django.utils.decorators import method_decorator
from django.views import View
from django.views.decorators.csrf import csrf_exempt
from logsapi.models.buckets import BucketModel
from logsapi.storage.miniostorage import MinioStorage


@method_decorator(csrf_exempt,  name='dispatch')
class Buckets(View):
    def __init__(self, **kwargs):
        super().__init__(**kwargs)
        self.minio = MinioStorage()

    def get(self, request, *args, **kwargs):
        buckets  = self.minio.list_buckets()
        return JsonResponse(buckets, safe=False)

    def post(self, request, *args, **kwargs):
        bucket = BucketModel.model_validate_json(request.body)
        success, msg = self.minio.create_bucket(bucket)
        if success:
            return JsonResponse({"msg": f"Bucket {bucket.name} created"}, status=200)
        return JsonResponse({"msg": msg}, status=302)