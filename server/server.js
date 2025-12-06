import 'dotenv/config';
import express from 'express';
import router from './router/routes.js';
import path from 'path';
import cors from 'cors';

const app = express();
const allowedOrigins = (process.env.ALLOWED_ORIGINS || '*')
  .split(',')
  .map((origin) => origin.trim())
  .filter(Boolean);

const corsOptions = {
  origin: (origin, callback) => {
    if (!origin || allowedOrigins.includes('*') || allowedOrigins.includes(origin)) {
      return callback(null, true);
    }
    return callback(new Error('Not allowed by CORS'));
  },
  credentials: true,
};
//be able to handle form data (spec for adding products)
app.use(express.urlencoded({ extended: true }));
app.use(express.json());
app.use(cors(corsOptions));
app.options('*', cors(corsOptions));


//telling it to serve public files
app.use(express.static('./public'));

//making it useable with docker
const OUTPUT_DIR = process.env.OUTPUT_DIR || 'outputCsv';
app.use('/results', express.static(path.resolve(OUTPUT_DIR)));

//mounting routers
// app.use("/", productRouter);

app.use("/", router);

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
  console.log(`Server is running on http://localhost:${PORT}`);
});


//exporting for testing purposes
export default app;
