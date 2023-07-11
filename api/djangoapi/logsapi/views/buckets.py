from django.http import JsonResponse
from django.utils.decorators import method_decorator
from django.views import View
from django.views.decorators.csrf import csrf_exempt
from logsapi.models.buckets import BucketModel
from logsapi.storage.miniostorage import MinioStorage


@method_decorator(csrf_exempt,  name='dispatch')
class Buckets(View, MinioStorage):
    def __init__(self, **kwargs):
        View.__init__(self, **kwargs)
        MinioStorage.__init__(self)

    def get(self, request, *args, **kwargs):
        buckets  = self.list_buckets()
        return JsonResponse(buckets, safe=False)

    def post(self, request, *args, **kwargs):
        bucket = BucketModel.model_validate_json(request.body)
        success, msg = self.create_bucket(bucket)
        if success:
            return JsonResponse({"msg": f"Bucket {bucket.name} created"}, status=200)
        return JsonResponse({"msg": msg}, status=302)

    def delete(self, request, *args, **kwargs):
        bucket = BucketModel.model_validate_json(request.body) 
        success = self.delete_bucket(bucket)
        if success:
            return JsonResponse({"msg": f"Bucket {bucket.name} removed"}, status=200)
        return JsonResponse({"msg": f"Bucket {bucket.name} could not be removed"}, status=500)