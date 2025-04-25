import { useEffect, useState } from 'react';
import axios from 'axios';
import Autocomplete from '@mui/material/Autocomplete';
import { LinearProgress, TextField, Typography } from '@mui/material';
import MapView from './components/MapView';

function App() {
  const [data, setData] = useState<{ id: number, line: string, location: string }[]>([{
    id: 0,
    line: 'loading...',
    location: 'loading...'
  }])
  const [input, setInput] = useState<string | null>(null)
  const [selected, setSelected] = useState<{ id: number, line: string, location: string } | null>(null)
  const [mapImg, setMapImg] = useState<string | null>(null)
  const [center, setCenter] = useState<{ x: number, y: number }>({ x: 0, y: 0 })
  const [loading, setLoading] = useState<boolean>(false)

  useEffect(() => {
    console.log('input', input)
    if (input === null) {
      return
    }
    axios.get('/search?line=' + input).then((res) => {
      console.log(res.data)
      setData(res.data)
    })
      .catch((err) => {
        console.error(err)
      }
      )
  }
    , [input])

  const handleSelect = async (v: { id: number, line: string, location: string }) => {
    try {
      setLoading(true)
      const data = await axios.post('/getmap', {
        room: v.line,
        location: v.location
      })
      setLoading(false)
      console.log(data.data)
      setMapImg(data.data.image)
      setCenter({
        x: data.data.cordinates[0],
        y: data.data.cordinates[1]
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
            setSelected(v)
            if (v) handleSelect(v)
          }
        }}
        onInputChange={(_e, v, r) => {
          console.log('input', v)
          if (r === 'input') {
            setInput(v)

          }

        }}
        getOptionLabel={(option) => option.line}
        getOptionKey={(option) => option.id}
        style={{ width: "80vw", margin: "10px 0px 20px 10px" }}
        renderInput={(params) => <TextField {...params} label="search room" variant="outlined" />}
      />
      <div>
        {selected && <div style={{ marginLeft: '10px' }}>
          <Typography variant="h6">Campus: {selected?.location.split('\\').at(-4)}</Typography>
          <Typography variant="h6">Building: {selected?.location.split('\\').at(-3)}</Typography>
          <Typography variant="h6">Floor: {selected?.location.split('\\').at(-2)}</Typography>
        </div>}
        {selected && <MapView map={`data:image/jpeg;base64,${mapImg}`} center={center} />}
      </div>
    </>
  )
}

export default App
