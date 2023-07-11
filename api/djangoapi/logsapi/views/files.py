from typing import Any
from django.http import HttpResponse, JsonResponse
from django.views import View
from django.utils.decorators import method_decorator
from django.views.decorators.csrf import csrf_exempt
from logsapi.models.forms.fileupload import UploadFile
from logsapi.storage.miniostorage import MinioStorage

@method_decorator(csrf_exempt,  name='dispatch')
class Files(View, MinioStorage):
    def __init__(self, **kwargs: Any) -> None:
        View.__init__(self, **kwargs)
        MinioStorage.__init__(self)

    def post(self, request, *args, **kwargs):
        if 'name' in kwargs:
            form = UploadFile(request.POST,request.FILES)
            if form.is_valid():
                uploaded = self.upload_file(request.FILES["file"], kwargs['name'])
                if uploaded:
                    return JsonResponse({"msg": "File uploaded"}, status=200)
                return JsonResponse({"msg": "Bucket not found"}, status=404)
            return JsonResponse({"msg": "Invalid data"}, status=404)
        return JsonResponse({"msg": "Invalid data"}, status=404)