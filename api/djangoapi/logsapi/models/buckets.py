from pydantic import BaseModel, Field


class BucketModel(BaseModel):
    name: str = Field(alias="name")
