-- CreateTable
CREATE TABLE "FloorData" (
    "id" SERIAL NOT NULL,
    "line" TEXT NOT NULL,
    "location" TEXT NOT NULL,

    CONSTRAINT "FloorData_pkey" PRIMARY KEY ("id")
);
