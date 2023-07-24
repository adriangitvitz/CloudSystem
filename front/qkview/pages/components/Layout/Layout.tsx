import { Box, useMediaQuery } from "@mui/material";
import { useState } from "react";
import Head from "next/head";

const Layout = (props: any) => {
    const isNonMobile = useMediaQuery("(min-width: 600px)");
    return (
        <>
            <Head>
                <title>QKVIEW</title>
                <meta name="description" content="Caresoft" />
                <meta name="viewport" content="width=device-width, initial-scale=1" />
            </Head>
            <main>
                {props.children}
            </main>
        </>
    )
}

export default Layout;
