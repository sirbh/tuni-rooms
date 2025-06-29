import { useEffect, useState } from 'react';
import axios from './utility/axios';
import Autocomplete from '@mui/material/Autocomplete';
import { LinearProgress, TextField, Typography } from '@mui/material';
import MapView from './components/MapView';

function App() {
  const [data, setData] = useState<{ id: number, room: string, fileLocation: string }[]>([{
    id: 0,
    room: 'loading...',
    fileLocation: 'loading...'
  }])
  const [input, setInput] = useState<string | null>(null)
  const [selected, setSelected] = useState<{ id: number, room: string, fileLocation: string } | null>(null)
  const [mapImg, setMapImg] = useState<string | null>(null)
  const [center, setCenter] = useState<{ x: number, y: number }>({ x: 0, y: 0 })
  const [loading, setLoading] = useState<boolean>(false)


  useEffect(() => {
    console.log('input', input)
    if (input === null) {
      return
    }
    console.log('input', "I was here")
    axios.get('/search?room=' + input).then((res) => {
      console.log(res.data)
      setData(res.data)
    })
      .catch((err) => {
        console.error(err)
      }
      )
  }
    , [input])

  const handleSelect = async (v: { id: number, room: string, fileLocation: string }) => {
    try {
      setLoading(true)
      const data = await axios.get('/getmap', {
        params:{
          room:v.room,
          location:v.fileLocation.replace('C:\\Users\\soura\\OneDrive\\Desktop\\node\\maps\\', '').replace('.txt', '.pdf')
        }
      } )
      setLoading(false)
      setMapImg(data.data.image)
      console.log('data', data.data.coordinates)
      setCenter({
        x: data.data.coordinates[0],
        y: data.data.coordinates[1]
      })

    }
    catch (e) {
      console.error(e)
    }
  }

  return (
    <>
      {loading && <LinearProgress />}
      <Autocomplete
        id="combo-box-demo"
        options={data}
        onChange={(_e, v, r) => {
          if (r === 'selectOption') {
            console.log('selected', v)
            setSelected(v)
            if (v) handleSelect(v)
          }
        }}
        onInputChange={(_e, v, r) => {
          if (r === 'input') {
            setInput(v)

          }

        }}
        getOptionLabel={(option) => {
          return option.room
        }}
        getOptionKey={(option) => option.id}
        style={{ width: "80vw", margin: "10px 0px 20px 10px" }}
        renderInput={(params) => <TextField {...params} label="search room" variant="outlined" />}
      />
      <div>
        {selected && <div style={{ marginLeft: '10px' }}>
          <Typography variant="h6">Campus: {selected?.fileLocation.split('\\')[selected?.fileLocation.split('\\').length - 4]}</Typography>
          <Typography variant="h6">Building: {selected?.fileLocation.split('\\')[selected?.fileLocation.split('\\').length - 3]}</Typography>
          <Typography variant="h6">Floor: {selected?.fileLocation.split('\\')[selected?.fileLocation.split('\\').length - 2]}</Typography>
        </div>}
        {selected && <MapView map={`data:image/jpeg;base64,${mapImg}`} center={center} />}
      </div>
    </>
  )
}

export default App
