package routes

import (
	"context"
	"fmt"
	"goapi/binders"
	"goapi/connections"
	"io"
	"net/http"

	"github.com/gin-gonic/gin"
	"github.com/minio/minio-go/v7"
)

const MAX_FILE_SIZE = 70 * 1024 * 1024 // 70MB

func Download(c *gin.Context) {
	ctx, cancel := context.WithCancel(context.Background())
	defer cancel()
	var object binders.Object
	if err := c.ShouldBindUri(&object); err != nil {
		c.JSON(500, gin.H{"message": err.Error()})
		return
	}
	found, err := connections.MINIO.BucketExists(ctx, object.Bucket)
	if !found {
		c.JSON(http.StatusConflict, gin.H{
			"message": fmt.Sprintf("Bucket %s not found", object.Bucket),
		})
		return
	}
	if err != nil {
		c.JSON(500, gin.H{"message": err.Error()})
		return
	}
	obj, err := connections.MINIO.GetObject(ctx, object.Bucket, object.Name, minio.GetObjectOptions{})
	if err != nil {
		c.JSON(500, gin.H{"message": err.Error()})
		return
	}
	defer obj.Close()
	rw := c.Writer
	rw.Header().Set("Content-Disposition", fmt.Sprintf("attachment; filename=%s", object.Name))
	_, err = io.Copy(rw, obj)
	if err != nil {
		c.JSON(500, gin.H{"message": err.Error()})
		return
	}
}

func Upload(c *gin.Context) {
	ctx, cancel := context.WithCancel(context.Background())
	defer cancel()
	var bucketinfo binders.Bucketinfo
	if err := c.ShouldBindUri(&bucketinfo); err != nil {
		c.JSON(500, gin.H{"message": err.Error()})
		return
	}
	r := c.Request
	reader, err := r.MultipartReader()
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"message": fmt.Sprintf("Error in reader init: %s", err.Error())})
		return
	}
	for {
		chunk, err := reader.NextPart()
		if err == io.EOF {
			break
		}
		if err != nil {
			c.JSON(http.StatusInternalServerError, gin.H{"message": fmt.Sprintf("Error in next reader: %s", err.Error())})
			return
		}
		if chunk.FileName() != "" {
			_, err := connections.MINIO.PutObject(ctx, bucketinfo.Name, chunk.FileName(), chunk, -1, minio.PutObjectOptions{
				ContentType: chunk.Header.Get("Content-Type"),
			})
			if err != nil {
				c.JSON(http.StatusInternalServerError, gin.H{"message": fmt.Sprintf("Error uploading: %s", err.Error())})
				return
			}
		}
		defer chunk.Close()
	}
	c.JSON(http.StatusOK, gin.H{"message": "file uploaded"})
}
