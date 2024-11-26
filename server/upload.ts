import fs from 'fs';
import path from 'path';
import csv from 'csv-parser';
import { PrismaClient } from '@prisma/client';

const prisma = new PrismaClient();
const csvFilePath = path.join(__dirname, 'output.csv'); // Update with your CSV file path

async function main() {
  const records:{line:string,location:string}[] = [];

  // Read the CSV file
  fs.createReadStream(csvFilePath)
    .pipe(csv())
    .on('data', (row) => {
      records.push({
        line: row.Line,
        location: row.Location,
      });
    })
    .on('end', async () => {
      // Insert records into the database
      for (const record of records) {
        await prisma.floorData.create({
          data: {
            line: record.line,
            location: record.location,
          },
        });
      }

      console.log('Data has been uploaded successfully.');
      await prisma.$disconnect();
    });
}

// Run the main function
main().catch((e) => {
  console.error(e);
  process.exit(1);
});
