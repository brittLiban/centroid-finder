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

// Serve processed CSV results at /results
app.use('/results', express.static(path.resolve('outputCsv')));

//mounting routers
// app.use("/", productRouter);

app.use("/", router);

app.listen(3001, () => {
    console.log(`Server is running on http://localhost:3001`);
});

//exporting for testing purposes
export default app;
