package core

import (
	"testing"
	"sort"
	"reflect"
)

func (password *Password) Len() int {
	return PasswordLength
}

func (password *Password) Less(i, j int) bool {
	return password[i] < password[j]
}

func (password *Password) Swap(i, j int) {
	password[i], password[j] = password[j], password[i]
}

func TestRandPassword(t *testing.T) {
	password := RandPassword()
	t.Log(password)
	sort.Sort(password)
	for i := 0; i < PasswordLength; i++ {
		if password[i] != byte(i) {
			t.Error("不能出现任何一个重复的byte位，必须又 0-255 组成，并且都需要包含")
		}
	}
}

func TestPasswordString(t *testing.T) {
	password := RandPassword()
	passwordStr := password.String()
	decodePassword, err := ParsePassword(passwordStr)
	if err != nil {
		t.Error(err)
	} else {
		if !reflect.DeepEqual(password, decodePassword) {
			t.Error("密码转化成字符串后反解后数据不对应")
		}
	}
}
