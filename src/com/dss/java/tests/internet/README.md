### UDP 数据报，接收数据
```byte[] bytes = new byte[1024];\
DatagramPacket datagramPacket = new DatagramPacket(bytes, 0, bytes.length);
datagramSocket.receive(datagramPacket);
byte[] data = datagramPacket.getData();
```
* bytes : 接收的数据报中的参数，bytes.length : 长度等于声明的字节数组长度
* data : 通过数据报调用方法 getData() 所得 data.length :  长度等于声明的字节数组的长度
* length : 接收到的字节数组的有效数据长度，即如果发送的数据长度小于字节数组的长度，就是数据的实际长度；如果发送的数据长度大于声明的字节数组的长度，就是数组的长度
