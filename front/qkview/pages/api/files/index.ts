import {NextApiRequest, NextApiResponse} from "next";
import Cors from 'cors';
import {PrismaClient} from "@prisma/client";

const cors = Cors({
    methods: ["GET"]
})

function runMiddleware(req: NextApiRequest, res: NextApiResponse, fn: Function) {
    return new Promise((resolve, reject) => {
        fn(req, res, (result: any) => {
            if (result instanceof Error) {
                return reject(result)
            }
            return resolve(result)
        })
    })
}

export default async function handler(req: NextApiRequest, res: NextApiResponse) {
    await runMiddleware(req, res, cors);
    const prisma = new PrismaClient();
    const files = await prisma.uploads.findMany();
    res.json(JSON.parse(JSON.stringify(
        files,
        (key, value) => (typeof value === 'bigint' ? value.toString() : value) // return everything else unchanged
    )));
}