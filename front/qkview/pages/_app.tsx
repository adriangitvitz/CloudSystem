import {AppProps} from "next/app";
import {CacheProvider, EmotionCache} from "@emotion/react";
import {ThemeProvider} from "@mui/material";
import {CssBaseline} from "@mui/material";
import createEmotionCache from "@/pages/emotioncache";
import theme from "@/pages/theme";
import Layout from "@/pages/components/Layout";

const emotioncache = createEmotionCache();

export interface MAppProps extends AppProps {
    emotionCache?: EmotionCache
}

export default function App(props: MAppProps) {
    const {Component, emotionCache = emotioncache, pageProps} = props;
    return (
        <CacheProvider value={emotionCache}>
            <ThemeProvider theme={theme}>
                <CssBaseline/>
                <Layout>
                    <Component {...pageProps}/>
                </Layout>
            </ThemeProvider>
        </CacheProvider>
    )
}