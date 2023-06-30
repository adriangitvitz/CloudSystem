package connections

import (
	"log"
	"os"

	"github.com/minio/minio-go/v7"
)

var MINIO *minio.Client

func Connectminio(opt *minio.Options) {
    minioclient, err := minio.New(os.Getenv("ENDPOINT"), opt)
    if err != nil {
        log.Fatalf("Error connecting to MINIO: %s", err.Error())
    }
    MINIO = minioclient
}
