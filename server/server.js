import express from 'express';
import path from 'path';


const app = express();
//be able to handle form data (spec for adding products)
app.use(express.urlencoded({ extended: true }));
app.use(express.json());

//telling it to serve public files
app.use(express.static('./public'));

// //need to set pug as view engine 
// app.set('view engine', 'pug');
// //telling it where to access files.
// app.set('views', path.resolve('./views'));


//mounting routers
// app.use("/", productRouter);


app.listen(3000, () => {
    console.log(`Server is running on http://localhost:3000`);

});