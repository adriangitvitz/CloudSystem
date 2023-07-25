'use client'

import {Container, Typography} from "@mui/material";
import {useCallback, useState} from "react";
import {useDropzone} from "react-dropzone";


interface DropzoneProps {
    onUpload: (file: File) => void;
    color: string;
    statustext: string | null
}

const DropzoneUp = ({onUpload, color, statustext}: DropzoneProps) => {
    const [filename, setFilename] = useState("");

    const onDrop = useCallback((accepted: File[]) => {
        setFilename(accepted[0].name);
        onUpload(accepted[0]);
    }, [onUpload]);

    const {getRootProps, getInputProps, isDragActive} = useDropzone({
        onDrop: onDrop,
        disabled: ((statustext === "Processing"))
    });

    // useEffect(() => {
    //    const n_ws = new WebSocket(`${process.env.WSSERVER}`);
    //    n_ws.onerror = err => console.log(err);
    //    n_ws.onopen = () => setWS(n_ws);
    //    n_ws.onmessage = msg => setData(msg.data);
    // }, [])
    return (
        <Container {...getRootProps()} sx={{
            flex: 1,
            display: "flex",
            flexDirection: "column",
            alignItems: "center",
            justifyContent: "center",
            height: "100%",
            backgroundColor: color,
            padding: "20px",
        }}>
            <input {...getInputProps()} />
            <Typography variant="h5" sx={{fontWeight: "bold"}}>
                {isDragActive ? (
                    "Drop the file..."
                ) : (
                    statustext ? `${statustext}` : "Drag and drop the qkview file"
                )}
            </Typography>
        </Container>
    );
}

export default DropzoneUp;
