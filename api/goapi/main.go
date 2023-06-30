package main

import (
	"goapi/connections"
	"goapi/routes"
	"os"

	"github.com/gin-gonic/gin"
	"github.com/joho/godotenv"
	"github.com/minio/minio-go/v7"
	"github.com/minio/minio-go/v7/pkg/credentials"
)

func main() {
	godotenv.Load()
	connections.Connectminio(&minio.Options{
		Creds:  credentials.NewStaticV4(os.Getenv("ACCESSKEY"), os.Getenv("SECRETKEY"), ""),
		Secure: false,
	})
	r := gin.Default()
	r.SetTrustedProxies([]string{"localhost"})
	// TODO: Authentication and Authorization
	// auth := r.Group("/auth")
	v1 := r.Group("/v1")
	files := v1.Group("/files")
	{
		files.GET(":bucket/:name", routes.Download)
        files.PUT(":name", routes.Upload)
	}
	buckets := v1.Group("/buckets")
	{
		buckets.GET("/", routes.Getbuckets)
		buckets.POST("/", routes.Createbucket)
		buckets.DELETE(":name", routes.Deletebucket)
		buckets.GET(":name/files", routes.Getbucketfiles)
	}
	r.Run(":3000")
}
