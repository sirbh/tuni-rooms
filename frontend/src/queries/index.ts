import axios from "@/lib/axios";

export interface IMapOptions {
  id: string;
  room: string;
  fileLocation: string;
}
export const getMapSerachQuery = (search: string) => {
  return () => {
    return axios
      .get<IMapOptions[]>(`/search?room=${search}`)
      .then((response) => response.data)
      .catch((error) => {
        console.error("Error fetching map search data:", error);
        throw error;
      });
  };
};

export const getMapByIdQuery = (options: IMapOptions) => {
  return () => {
    return axios.get("/getmap", {
      params: {
        room: options.room,
        location: options.fileLocation
          .replace("C:\\Users\\soura\\OneDrive\\Desktop\\node\\maps\\", "")
          .replace(".txt", ".pdf"),
      },
    });
  };
};
