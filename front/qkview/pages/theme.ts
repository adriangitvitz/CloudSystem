import {createTheme, PaletteMode} from "@mui/material";

export const colortokens = () => ({
    'primary': {
        DEFAULT: '#0d47a1',
        50: '#E3F2FD',
        100: '#BBDEFB',
        200: '#90CAF9',
        300: '#64B5F6',
        400: '#42A5F5',
        500: '#2196F3',
        600: '#1E88E5',
        700: '#1976D2',
        800: '#1565C0',
        900: '#0D47A1'
    },
    'secondary': {
        DEFAULT: '#00897b',
        50: '#E0F2F1',
        100: '#B2DFDB',
        200: '#80CBC4',
        300: '#4DB6AC',
        400: '#26A69A',
        500: '#009688',
        600: '#00897B',
        700: '#00796B',
        800: '#00695C',
        900: '#004D40'
    },
    'background': {
        DEFAULT: '#212121',
        50: '#9E9E9E',
        100: '#757575',
        200: '#616161',
        300: '#424242',
        400: '#333333',
        500: '#212121',
        600: '#191919',
        700: '#121212',
        800: '#0D0D0D',
        900: '#000000'
    },
    'text': {
        DEFAULT: '#ffffff',
        50: '#ffffff',
        100: '#E0E0E0',
        200: '#BDBDBD',
        300: '#9E9E9E',
        400: '#757575',
        500: '#616161',
        600: '#424242',
        700: '#303030',
        800: '#212121',
        900: '#000000'
    },
});


export const themesettings = () => {
    const colors = colortokens();
    const mode: PaletteMode = 'dark';
    return {
        palette: {
            mode: mode,
            primary: {
                ...colors.primary,
                main: colors.primary[100]
            },
            neutral: {
                ...colors.text,
                main: colors.text[500]
            },
            secondary: {
                ...colors.secondary,
                main: colors.secondary[500]
            },
            background: {
                ...colors.background,
                default: colors.background[500]
            }
        },
        typography: {
            fontFamily: ["Inter", "sans-serif"].join(","),
            fontSize: 12,
            h1: {
                fontFamily: ["Inter", "sans-serif"].join(","),
                fontSize: 40,
            },
            h2: {
                fontFamily: ["Inter", "sans-serif"].join(","),
                fontSize: 32,
            },
            h3: {
                fontFamily: ["Inter", "sans-serif"].join(","),
                fontSize: 24,
            },
            h4: {
                fontFamily: ["Inter", "sans-serif"].join(","),
                fontSize: 20,
            },
            h5: {
                fontFamily: ["Inter", "sans-serif"].join(","),
                fontSize: 16,
            },
            h6: {
                fontFamily: ["Inter", "sans-serif"].join(","),
                fontSize: 14,
            },
        }
    }
};

const theme = createTheme(themesettings(), []);
export default theme;
