# Step 1: Use a base image with Node.js
FROM node:18-alpine

# Step 2: Set the working directory inside the container
WORKDIR /app

# Step 3: Copy the package.json and package-lock.json (if present) to the working directory
COPY package*.json ./

# Step 4: Install the dependencies
RUN npm install 

# Step 5: Copy the entire application to the container's working directory
COPY . .

RUN npx prisma generate

CMD [ "npm","start" ]

