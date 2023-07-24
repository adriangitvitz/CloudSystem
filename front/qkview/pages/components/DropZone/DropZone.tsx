'use client'

import { Container, Paper, Typography } from "@mui/material";
import { useCallback, useState } from "react";
import { useDropzone } from "react-dropzone";

interface DropzoneProps {
    onUpload: (file: File) => void;
}

const DropzoneUp = ({ onUpload }: DropzoneProps) => {
    const [filename, setFilename] = useState("");
    const onDrop = useCallback((accepted: File[]) => {
        setFilename(accepted[0].name);
        onUpload(accepted[0]);
    }, [onUpload]);
    const { getRootProps, getInputProps, isDragActive } = useDropzone({ onDrop });
    return (
        <Container {...getRootProps()} sx={{
            flex: 1,
            display: "flex",
            flexDirection: "column",
            alignItems: "center",
            justifyContent: "center",
            height: "100%",
            backgroundColor: "transparent",
            padding: "20px",
        }}>
            <input {...getInputProps()} />
            <Typography variant="h5" sx={{ fontWeight: "bold" }}>
                {isDragActive ? (
                    "Ready to upload files..."
                ) : (
                    filename ? `${filename}` : "Drag and drop the qkview file"
                )}
            </Typography>
        </Container>
    );
}

export default DropzoneUp;
