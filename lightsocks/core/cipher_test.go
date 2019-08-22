package core

import (
	"testing"
	"reflect"
	"crypto/rand"
)

const (
	MB = 1024 * 1024
)

// 测试 Cipher 加密解密
func TestCipher(t *testing.T) {
	password := RandPassword()
	t.Log(password)
	cipher := NewCipher(password)
	// 原数据
	org := make([]byte, PasswordLength)
	for i := 0; i < PasswordLength; i++ {
		org[i] = byte(i)
	}
	// 复制一份原数据到 tmp
	tmp := make([]byte, PasswordLength)
	copy(tmp, org)
	t.Log(tmp)
	// 加密 tmp
	cipher.encode(tmp)
	t.Log(tmp)
	// 解密 tmp
	cipher.decode(tmp)
	t.Log(tmp)
	if !reflect.DeepEqual(org, tmp) {
		t.Error("解码编码数据后无法还原数据，数据不对应")
	}
}

func BenchmarkEncode(b *testing.B) {
	password := RandPassword()
	cipher := NewCipher(password)
	bs := make([]byte, MB)
	b.ResetTimer()
	rand.Read(bs)
	cipher.encode(bs)
}

func BenchmarkDecode(b *testing.B) {
	password := RandPassword()
	cipher := NewCipher(password)
	bs := make([]byte, MB)
	b.ResetTimer()
	rand.Read(bs)
	cipher.decode(bs)
}
