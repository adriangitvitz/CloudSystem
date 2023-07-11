from pydantic import BaseModel, Field, validator
from typing import Optional
import datetime

class BucketfilesModel(BaseModel):
    name: str = Field(alias="name")
    size: Optional[int] = Field(alias="size")
    latest: Optional[bool] = Field(alias="latest")
    modified: Optional[datetime.datetime] = Field(alias="modified")
    content_type: Optional[str] = Field(alias="contenttype")

    @validator("latest")
    def set_latest(cls, latest):
        return latest or False
    
    @validator("content_type")
    def set_content_type(cls, content_type):
        return content_type or ""