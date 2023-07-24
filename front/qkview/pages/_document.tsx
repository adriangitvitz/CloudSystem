import React, {JSX} from "react";
import createEmotionServer from "@emotion/server/create-instance";
import {AppType} from "next/app";
import theme from "@/pages/theme";
import createEmotionCache from "@/pages/emotioncache";
import {MAppProps} from "@/pages/_app";
import Document, {DocumentContext, Html, Main, NextScript, DocumentProps, Head} from "next/document";

interface DocProps extends DocumentProps {
    emotionStyleTags: JSX.Element[];
}

export default function Doc({emotionStyleTags}: DocProps) {
    return (
        <Html lang="en">
            <Head>
                <meta name="theme" content={theme.palette.primary.main}/>
                <meta name="emotion-insertion-point" content=""/>
                {emotionStyleTags}
            </Head>
            <body>
            <Main/>
            <NextScript/>
            </body>
        </Html>
    )
}

Doc.getInitialProps = async (ctx: DocumentContext) => {
    const origrender = ctx.renderPage;
    const cache = createEmotionCache();
    const {extractCriticalToChunks} = createEmotionServer(cache);
    ctx.renderPage = () =>
        origrender({
            enhanceApp: (App: React.ComponentType<React.ComponentProps<AppType> & MAppProps>) =>
                function EnhanceApp(props) {
                    return <App emotionCache={cache} {...props}/>;
                }
        });
    const initialProps = await Document.getInitialProps(ctx);
    const emotionStyles = extractCriticalToChunks(initialProps.html);
    const emotionStyleTags = emotionStyles.styles.map((style) => (
        <style
            data-emotion={`${style.key} ${style.ids.join(' ')}`}
            key={style.key}
            dangerouslySetInnerHTML={{__html: style.css}}
        />
    ));
    return {
        ...initialProps,
        emotionStyleTags
    }
}