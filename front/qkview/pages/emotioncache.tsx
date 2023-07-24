import createCache from "@emotion/cache";

const isBrowser = typeof document != "undefined";

export default function createEmotionCache() {
    let insertionPoint;
    if(isBrowser)  {
        const emotioninsert = document.querySelector<HTMLMetaElement>(
            'meta[name="emotion-insertion-point"]',
        );
        insertionPoint = emotioninsert ?? undefined;
    }
    return createCache({key: 'mui-style', insertionPoint});
}