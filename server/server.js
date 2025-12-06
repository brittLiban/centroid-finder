import 'dotenv/config';
import express from 'express';
import router from './router/routes.js';
import path from 'path';
import cors from 'cors';

const app = express();
//be able to handle form data (spec for adding products)
app.use(express.urlencoded({ extended: true }));
app.use(express.json());
app.use(cors());


//telling it to serve public files
app.use(express.static('./public'));

//making it useable with docker
const OUTPUT_DIR = process.env.OUTPUT_DIR || 'outputCsv';
app.use('/results', express.static(path.resolve(OUTPUT_DIR)));

//simple health endpoint (easy to test + proves server is up)
app.get('/health', (req, res) => {
  res.status(200).json({ status: 'ok' });
});

//mounting routers
// app.use("/", productRouter);

app.use("/", router);

const PORT = process.env.PORT || 3000;

//don’t listen during tests
if (process.env.NODE_ENV !== 'test') {
  app.listen(PORT, () => {
    console.log(`Server is running on http://localhost:${PORT}`);
  });
}


//exporting for testing purposes
export default app;
