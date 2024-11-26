import express, { Request, Response } from 'express';
import { PrismaClient } from '@prisma/client';
import cors from 'cors';
import axios from './utility/axios';




const app = express();
const port = process.env.PORT || 3000;

const prisma = new PrismaClient();

app.use(cors());

// Middleware to parse JSON
app.use(express.json());

app.use(express.static("public"));

// Search route
app.get('/search', async (req: Request, res: Response) => {
    const { line } = req.query;
  
    try {
      const results = await prisma.floorData.findMany({
        where: {
          line: {
            contains: line as string,
            mode: 'insensitive', // Case insensitive search
          },
        },
        take: 10, // Limit results to the first 10 records
      });
      res.json(results);
    } catch (error) {
      console.error(error); // Log the error for debugging
      res.status(500).json({ error: 'An error occurred while searching' });
    }
  });

app.post('/getmap', async (req: Request, res: Response) => {
    const { room,location } = req.body;
    
    try {
      const data = await axios.post('/post',{
          room,
          location: location.replace('C:\\Users\\soura\\OneDrive\\Desktop\\node\\maps\\', '').replace('.txt', '.pdf')
      })


      res.json({ room, location, image: data.data.image, cordinates: data.data.coordinates });

    } catch (error) {
      console.error(error); // Log the error for debugging
      res.status(500).json({ error: 'An error occurred while searching' });
    }
  

  });

app.listen(port, () => {
  console.log(`Server is running at http://localhost:${port}`);
});
