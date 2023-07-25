import {Box, Container, Typography, useMediaQuery} from "@mui/material";
import UploadForm from "./components/UploadForm/UploadForm";
import {DataGrid} from "@mui/x-data-grid";
import useSWRSubscription from "swr/subscription";
import {useState} from "react";

export default function Home() {
    const isNonMediumScreens = useMediaQuery("(min-width: 1200px)");
    const [tabledata, setTabledata] = useState([]);

    const columns = [
        {
            field: "id",
            headerName: "ID",
            flex: 1
        },
        {
            field: "bucket",
            headerName: "Bucket",
            flex: 1
        },
        {
            field: "filename",
            headerName: "File Name",
            flex: 1
        },
        {
            field: "size",
            headerName: "Size",
            flex: 1
        }
    ]

    return (
        <Box m="1.5rem 2.5rem">
            <Box
                mt="20px"
                display="grid"
                gridTemplateColumns="repeat(12,1fr)"
                gridAutoRows="100px"
                gap="20px"
                sx={{
                    "& > div": {gridColumn: isNonMediumScreens ? undefined : "span 12"}
                }}
            >
                <UploadForm setTabledata={setTabledata}/>
                <Box
                    gridColumn="span 12"
                    gridRow="span 3"
                    sx={{
                        "& .MuiDataGrid-root": {
                            border: "none",
                            borderRadius: "5rem"
                        },
                        "& .MuiDataGrid-cell": {
                            borderBottom: "none"
                        },
                        "& .MuiDataGrid-columnHeaders": {
                            backgroundColor: "background.600",
                            color: "background.100",
                            borderBottom: "none"
                        },
                        "& .MuiDataGrid-virtualScroller": {
                            backgroundColor: "background.600"
                        },
                        "& .MuiDataGrid-footerContainer": {
                            backgroundColor: "background.600",
                            color: "primary.100",
                            borderTop: "none"
                        },
                        "& .MuiDataGrid-toolbarContainer .MuiButton-text": {
                            color: "background.100 !important"
                        }
                    }}
                >
                    <DataGrid
                        columns={columns}
                        rows={tabledata}
                        getRowId={(row: any) => row.id}
                    />
                </Box>
            </Box>
        </Box>
    )
}
