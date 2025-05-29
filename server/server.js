import express from 'express';
import router from './router/routes.js'
import path from 'path';


const app = express();
//be able to handle form data (spec for adding products)
app.use(express.urlencoded({ extended: true }));
app.use(express.json());

//telling it to serve public files
app.use(express.static('./public'));


//mounting routers
// app.use("/", productRouter);

app.use("/", router)
app.listen(3000, () => {
    console.log(`Server is running on http://localhost:3000`);

});

//exporting for testing purposes
export default app; 