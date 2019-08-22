[![Build Status](https://img.shields.io/travis/gwuhaolin/lightsocks.svg?style=flat-square)](https://travis-ci.org/gwuhaolin/lightsocks)

# Lightsocks
一个轻量级网络混淆代理，基于 SOCKS5 协议，可用来代替 Shadowsocks。

- 只专注于混淆，用最简单高效的混淆算法达到目的；
- 不会放大传输流量，传输流量更少更快，占用资源更少；
- 纯 Golang 编写，跨平台。

## 安装
去 [releases](https://github.com/gwuhaolin/lightsocks/releases) 页下载最新的可执行文件，注意选择正确的操作系统和位数。
解压后会看到2个可执行文件，分别是：

- **lightsocks-local**：用于运行在本地电脑的客户端，用于桥接本地浏览器和远程代理服务，传输前会混淆数据；
- **lightsocks-server**：用于运行在代理服务器的客户端，会还原混淆数据；

## 启动
#### 启动 lightsocks-server
在墙外服务器下载好 lightsocks-server 后，执行命令：
```bash
./lightsocks-server
```
就可启动服务端，启动成功后会输出如下日志：
```
本地监听地址 listen：
:54261
密码 password：
******
```
假如服务器的 IP 是 45.56.76.5，则以上日志的含义是指：

- 服务监听在 `45.56.76.5:54261`，监听端口会随机生成；
- 使用的密码是  `******`

#### 启动 lightsocks-local
在本地电脑下载好 lightsocks-local 后，执行命令：
```bash
./lightsocks-local
```
就可启动本地代理客户端，会看到如下日志：
```bash
2017/10/11 10:03:16 保存配置到文件 /Users/username/.lightsocks.json 成功
2017/10/11 10:03:16 lightsocks-client:master 启动成功 监听在 [::]:7448
```
这表明生成了一份配置文件到 `/Users/username/.lightsocks.json`。
为了让客户端用指定的密码去连接服务器，需要给客户端传入参数，为此需要修改该配置文件为如下：
```json
{
  "remote": "45.56.76.5:54261",
  "password": "******"
}
```
重新启动 lightsocks-local 后，再使用监听在 `127.0.0.1:7448` 的本地 SOCK5 服务就可以正常使用了。
 
## 配置
#### lightsocks-local 支持的选项：
- **password**：用于加密数据的密码，字符串格式，在没有填时会自动生成；
- **listen**：本地 SOCKS5 代理客户端的监听地址，格式为 `ip:port`，默认为 `0.0.0.0:7448`；
- **remote**：墙外服务器的监听地址，格式为 `ip:port`，默认为 `0.0.0.0:*`。

#### lightsocks-server 支持的选项：
- **password**：用于加密数据的密码，字符串格式，在没有填时会自动生成；
- **listen**：本地 SOCKS5 代理客户端的监听地址，格式为 `ip:port`，默认为 `0.0.0.0:*`。

#### 注意：
- lightsocks-local 和 lightsocks-server 的 password 必须一致才能正常正常使用，password 不要泄露。
- password 会自动生成，不要自己生成。一般采用拷贝 lightsocks-server 生成的密码到 lightsocks-local 使用的本地配置文件中。


只能通过 JSON 文件的方式传参给 lightsocks-local 和 lightsocks-server，启动前会去 `~/.lightsocks.json` 文件中读取配置，启动后会把配置保存在 `~/.lightsocks.json` 文件中，
其格式为 JSON，内容大致如下：
```json
{
  "remote": "45.56.76.5:7448",
  "password": "******"
}
```





