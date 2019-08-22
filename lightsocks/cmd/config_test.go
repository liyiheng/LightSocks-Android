package cmd
//
//import (
//	"testing"
//	"os"
//)
//
//func TestReadConfig(t *testing.T) {
//	config := ReadConfig()
//	if config.Password == "" {
//		t.Error("返回的密码不能为空")
//	}
//	if config.ListenAddr == "" {
//		t.Error("返回的 ListenAddr 不能为空")
//	}
//	if config.RemoteAddr == "" {
//		t.Error("返回的 RemoteAddr 不能为空")
//	}
//	if _, err := os.Stat(configPath); os.IsNotExist(err) {
//		t.Error("配置文件没有生成")
//	}
//}
