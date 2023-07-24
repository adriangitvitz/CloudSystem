'use client'
import { Box } from "@mui/material";
import DropzoneUp from "../DropZone/DropZone";

export default function UploadForm() {
    const onUpload = async (file: File) => {
        console.log(file);
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
                backgroundColor: "background.400"
            }}
        >
            <Box sx={{ width: "100%" }}>
                <DropzoneUp onUpload={onUpload} />
            </Box>
        </Box>
    )
}
