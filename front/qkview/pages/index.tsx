import { Box, Container, Typography, useMediaQuery } from "@mui/material";
import UploadForm from "./components/UploadForm/UploadForm";
import { DataGrid } from "@mui/x-data-grid";

export default function Home() {
    const isNonMediumScreens = useMediaQuery("(min-width: 1200px)");
    return (
        <Box m="1.5rem 2.5rem">
            <Box
                mt="20px"
                display="grid"
                gridTemplateColumns="repeat(12,1fr)"
                gridAutoRows="100px"
                gap="20px"
                sx={{
                    "& > div": { gridColumn: isNonMediumScreens ? undefined : "span 12" }
                }}
            >
                <UploadForm />
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
                            backgroundColor: "background.700",
                            color: "background.100",
                            borderBottom: "none"
                        },
                        "& .MuiDataGrid-virtualScroller": {
                            backgroundColor: "background.700"
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
                        columns={[]}
                        rows={[]}
                    />
                </Box>
            </Box>
        </Box>
    )
}
