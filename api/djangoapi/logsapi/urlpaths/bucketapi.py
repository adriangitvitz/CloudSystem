from django.urls import path
from logsapi.views import buckets

urlpatterns = [
    path("buckets/", buckets.Buckets.as_view(), name="buckets")
]
