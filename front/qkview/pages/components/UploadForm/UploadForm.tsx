'use client'
import {Box} from "@mui/material";
import DropzoneUp from "../DropZone/DropZone";
import {useEffect, useState} from "react";
import useSWRSubscription from "swr/subscription";
import useSWR from "swr";

interface UploadFormProps {
    setTabledata: (data: []) => void;
}

export default function UploadForm({setTabledata}: UploadFormProps) {
    const [backcolor, setBackcolor] = useState<string>('background.400');
    const [statustext, setStatustext] = useState<string | null>(null);
    const [startfetch, setStartfetch] = useState<boolean>(false);

    const getall = (url: string) => fetch(url).then(r => r.json()).finally(() => setStartfetch(false));

    const {data: datasql} = useSWR(() => startfetch ? '/api/files' : null, getall);

    const datasocketlistener = (data: string) => {
        const statusk = ['in-process', 'processed'];
        if (statusk.includes(data)) {
            if (data === "in-process") {
                setBackcolor("warning.600");
                setStatustext("Processing");
            } else if (data === "processed") {
                setBackcolor("secondary.800");
                setStatustext("Done");
                setStartfetch(!startfetch);
            }
        }
    }

    const {data, error} = useSWRSubscription(`${process.env.WSSERVER}`, (key, {next}) => {
        const socket = new WebSocket(key);
        socket.addEventListener("message", (event) => next(null, event.data));
        return () => socket.close()
    })

    useEffect(() => {
        if (datasql) setTabledata(datasql);
    }, [datasql]);

    useEffect(() => {
        if (data) datasocketlistener(data);
    }, [data])

    const onUpload = async (file: File) => {
        const formdata = new FormData();
        formdata.append("file", file);
        const response = await fetch(`${process.env.FILEAPI}/${process.env.BUCKET}`, {
            method: "PUT",
            body: formdata,
            mode: "cors"
        });
        if (!response.ok) {
            console.log("Failed");
        } else {
            console.log("File uploaded");
        }
    }

    return (
        <Box
            gridColumn="span 4"
            gridRow="span 1"
            display="flex"
            flexDirection="column"
            justifyContent="space-between"
            p="1.25rem 1rem"
            flex="1 1 100%"
            borderRadius="0.55rem"
            sx={{
                backgroundColor: backcolor
            }}
        >
            <Box sx={{width: "100%"}}>
                <DropzoneUp onUpload={onUpload} color={backcolor} statustext={statustext}/>
            </Box>
        </Box>
    )
}
