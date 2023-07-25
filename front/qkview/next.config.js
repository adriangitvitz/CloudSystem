/** @type {import('next').NextConfig} */
const nextConfig = {
    env: {
        WSSERVER: process.env.WSSERVER,
        FILEAPI: process.env.FILEAPI,
        BUCKET: process.env.BUCKET
    }
}

module.exports = nextConfig
