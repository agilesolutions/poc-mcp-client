# MCP Client
The MCP Client is a key component in the Model Context Protocol (MCP) architecture, responsible for establishing and managing connections with MCP servers
 This client can provide data to AI models can load and use a predefined set of tools provided through the MCP Servers included on this application.

## Legend this demo

<img title="Model Context Protocol legend" alt="Alt text" src="/images/mcp.png">

## Load and run SMOLLM2 compact languague model on Google Colab
**Step 1:** Open your notebook at [https://colab.research.google.com/](https://colab.research.google.com/)

```
# Download and install ollama to the system
!curl -fsSL https://ollama.com/install.sh | sh 
```
**Step 2:** Install the Ollama Python SDK and pyngrok, the Python library that allows us to create secure tunnels
```
!pip install pyngrok ollama
```
**Step 3:** Start ollama server
```
import subprocess
def start_ollama_server() -> None:
    """Starts the Ollama server."""
    subprocess.Popen(['ollama', 'serve'])
    print("Ollama server started.")

start_ollama_server()
```
**Step 4:** Sign up for a [free ngrok](https://dashboard.ngrok.com/) account and get your authtoken. Store this authtoken securely in your Colab secrets (using the key icon on the left sidebar). Name the secret NGROK_AUTHTOKEN.
**Step 5:** Run this function to setting up the ngrok Tunnel, opening the secure door to your colab server. This will print out the full URL to remotely connect to ollama server running on colab.
```
from pyngrok import ngrok
from google.colab import userdata
def setup_ngrok_tunnel(port: str) -> ngrok.NgrokTunnel:
    ngrok_auth_token = userdata.get('NGROK_AUTHTOKEN')
    if not ngrok_auth_token:
        raise RuntimeError("NGROK_AUTHTOKEN is not set.")

    ngrok.set_auth_token(ngrok_auth_token)
    tunnel = ngrok.connect(port, host_header=f'localhost:{port}')
    print(f"ngrok tunnel created: {tunnel.public_url}")
    return tunnel
    
NGROK_PORT = '11434'
ngrok_tunnel = setup_ngrok_tunnel(NGROK_PORT)
```
**Step 5:** finally run your ollama model
```
!ollama run smollm2
```
**Step 6:** run this springboot app and hit one of the REST API endpionts
```
http://localhost:80/agent/with-tools
```
