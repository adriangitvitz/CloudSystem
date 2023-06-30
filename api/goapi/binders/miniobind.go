package binders

type Bucketinfo struct {
	Name string `uri:"name" binding:"required"`
}

type Object struct {
	Name   string `uri:"name" binding:"required"`
	Bucket string `uri:"bucket" binding:"required"`
}
