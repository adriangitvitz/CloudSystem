package models

import "time"

type Objects struct {
	Name        string    `json:"name"`
	Size        int64     `json:"size"`
	Latest      bool      `json:"latest"`
	Modified    time.Time `json:"modified"`
	ContentType string    `json:"contenttype"`
}
