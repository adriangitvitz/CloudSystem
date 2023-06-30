package routes

import (
	"context"
	"fmt"
	"goapi/binders"
	"goapi/connections"
	"goapi/models"
	"net/http"

	"github.com/gin-gonic/gin"
	"github.com/minio/minio-go/v7"
)

func Getbuckets(c *gin.Context) {
	ctx, cancel := context.WithCancel(context.Background())
	defer cancel()
	buckets, err := connections.MINIO.ListBuckets(ctx)
	if err != nil {
		c.JSON(500, gin.H{"message": err.Error()})
		return
	}
	c.SecureJSON(http.StatusOK, gin.H{
		"buckets": buckets,
	})
}

func Createbucket(c *gin.Context) {
	ctx, cancel := context.WithCancel(context.Background())
	defer cancel()
	var bucket models.Bucket
	if err := c.ShouldBindJSON(&bucket); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"message": err.Error()})
		return
	}
	found, err := connections.MINIO.BucketExists(ctx, bucket.Name)
	if found {
		c.JSON(http.StatusConflict, gin.H{
			"message": fmt.Sprintf("Bucket %s already exists", bucket.Name),
		})
		return
	}
	if err != nil {
		c.JSON(500, gin.H{"message": err.Error()})
		return
	}
	err = connections.MINIO.MakeBucket(ctx, bucket.Name, minio.MakeBucketOptions{})
	if err != nil {
		c.JSON(500, gin.H{"message": err.Error()})
		return
	}
	c.JSON(http.StatusCreated, gin.H{"message": fmt.Sprintf("Bucket %s created", bucket.Name)})
}

func Deletebucket(c *gin.Context) {
	ctx, cancel := context.WithCancel(context.Background())
	defer cancel()
	var bucketinfo binders.Bucketinfo
	if err := c.ShouldBindUri(&bucketinfo); err != nil {
		c.JSON(500, gin.H{"message": err.Error()})
		return
	}
	found, err := connections.MINIO.BucketExists(ctx, bucketinfo.Name)
	if err != nil {
		c.JSON(500, gin.H{"message": err.Error()})
		return
	}
	if !found {
		c.JSON(404, gin.H{"message": fmt.Sprintf("Bucket %s not found", bucketinfo.Name)})
		return
	}
	err = connections.MINIO.RemoveBucket(ctx, bucketinfo.Name)
	if err != nil {
		c.JSON(500, gin.H{"message": err.Error()})
		return
	}
	c.JSON(http.StatusOK, gin.H{"message": fmt.Sprintf("Bucket %s deleted", bucketinfo.Name)})
}

func Getbucketfiles(c *gin.Context) {
	ctx, cancel := context.WithCancel(context.Background())
	defer cancel()
	var bucketinfo binders.Bucketinfo
	if err := c.ShouldBindUri(&bucketinfo); err != nil {
		c.JSON(500, gin.H{"message": err.Error()})
		return
	}
	found, err := connections.MINIO.BucketExists(ctx, bucketinfo.Name)
	if err != nil {
		c.JSON(500, gin.H{"message": err.Error()})
		return
	}
	if !found {
		c.JSON(404, gin.H{"message": fmt.Sprintf("Bucket %s not found", bucketinfo.Name)})
		return
	}
	objects := connections.MINIO.ListObjects(ctx, bucketinfo.Name, minio.ListObjectsOptions{
		Recursive: true,
	})
	objects_found := make([]models.Objects, 0)
	for object := range objects {
		if object.Err != nil {
			c.JSON(500, gin.H{"message": object.Err.Error()})
			return
		}
		obj := models.Objects{
			Name:        object.Key,
			Size:        object.Size,
			Latest:      object.IsLatest,
			Modified:    object.LastModified,
			ContentType: object.ContentType,
		}
		objects_found = append(objects_found, obj)
	}
	c.SecureJSON(200, gin.H{
		"files": objects_found,
	})
}
