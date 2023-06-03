from http.server import BaseHTTPRequestHandler, HTTPServer
from urllib.parse import urlparse

hostName = "192.168.31.225"
serverPort = 8080

class MyServer(BaseHTTPRequestHandler):
    def do_GET(self):
        self.send_response(200)
        print(self.requestline)
        self.send_header("Content-type", "text/html")
        self.end_headers()
        self.wfile.write(bytes("<html><head><title>https://pythonbasics.org</title></head>", "utf-8"))
        self.wfile.write(bytes("<p>Request: %s</p>" % self.path, "utf-8"))
        self.wfile.write(bytes("<body>", "utf-8"))
        self.wfile.write(bytes("<p>This is an example web server.</p>", "utf-8"))
        self.wfile.write(bytes("</body></html>", "utf-8"))


    def do_POST(self):
        self.send_response(200)
        content_len = int(self.headers.get('Content-Length'))
        post_body = self.rfile.read(content_len)
        self.pars_args(post_body)

    def pars_args(self,msg):
        msg = msg.decode('UTF-8')
        msg.split('\n')
        print(msg)

if __name__ == "__main__":        
    webServer = HTTPServer((hostName, serverPort), MyServer)
    print("Server started http://%s:%s" % (hostName, serverPort))

    try:
        webServer.serve_forever()
    except KeyboardInterrupt:
        pass

    webServer.server_close()
    print("Server stopped.")