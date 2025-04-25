# MCP Client
The MCP Client is a key component in the Model Context Protocol (MCP) architecture, responsible for establishing and managing connections with MCP servers.
The MCP servers from this demo will enable LLM's to retrieve data from external tools (Person and Account details), plan actions like sending out notifications, generate documents, store records on external databases, and so on...

# LLM enhanced with augmentations such as retreivel, tools, memory.

<img title="Model Context Protocol legend" alt="Alt text" src="/images/mcp.png">

- Retrieval
  - Read Person details from RDMS through tools provided by **person-mcp-server**.
  - Read Account details for these persons from RDMS through tools provided by **account-mcp-server**
- Call 
  - Post notification with the responses returned provided by previous retrieval tool calls
  - Notification sent through tools provided by **notification-msp-server**
- Memory
  - Ability to store and retrieve data from past interactions
  - Learn and adapt: improve its performance over time through experience


## Ollama CLI Load and run SMOLLM2 compact languague model on Google Colab
The following steps allows you to run a LLM model with Ollama and connect that model over REST API from your SpringBoot application.

**Step 1:** Open a new notebook at [https://colab.research.google.com/](https://colab.research.google.com/)
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

**Step 5:** Run this function to setting up the ngrok Tunnel, opening the secure door to your colab server. This will print out the full URL to remotely connect to ollama server running on colab. Configure that URL as base-url on your Spring application.yaml property file.
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
**Step 6:** finally run your ollama model
```
!ollama run smollm2
```
**Step 7:** run this springBoot app and hit one of the REST API end-points
```
curl http://localhost/accounts/balance-by-person-id/20
```
