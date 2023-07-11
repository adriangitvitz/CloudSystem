from typing import Any
from django.http import JsonResponse
from django.http import HttpResponse
from django.views import View
from django.utils.decorators import method_decorator
from django.views.decorators.csrf import csrf_exempt
from logsapi.storage.miniostorage import MinioStorage
from logsapi.models.bucketfiles import BucketfilesModel

@method_decorator(csrf_exempt,  name='dispatch')
class BucketFiles(View, MinioStorage):
    def __init__(self, **kwargs: Any) -> None:
        View.__init__(self, **kwargs)
        MinioStorage.__init__(self)
    
    def get(self, request, *args, **kwargs):
        if 'name' in kwargs and kwargs['name']:
            find_bucket = self.client.bucket_exists(kwargs['name'])
            files = []
            if find_bucket:
                objects = self.client.list_objects(bucket_name=kwargs['name'], include_user_meta=True)
                for ob in objects:
                    ob_files = BucketfilesModel(
                        name=ob.object_name,
                        size=ob.size,
                        latest=ob.is_latest,
                        modified=ob.last_modified,
                        contenttype=ob.content_type
                    ).model_dump()
                    files.append(ob_files)
                return JsonResponse(files, status=200, safe=False)
            return JsonResponse({"msg": "Bucket not found"}, status=404)
        return JsonResponse({"msg": "No bucket specified"}, status=404)