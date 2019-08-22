#![allow(dead_code)]
use base64;
use rand::prelude::*;

pub struct Password(pub [u8; 256]);

impl std::string::ToString for Password {
    fn to_string(&self) -> String {
        base64::encode(&self.0[..])
    }
}

impl From<String> for Password {
    fn from(s: String) -> Password {
        let mut arr = [0u8; 256];
        let dat: Vec<u8> = base64::decode(&s).unwrap_or_default();
        for (&x, p) in dat.iter().zip(arr.iter_mut()) {
            *p = x;
        }
        Password(arr)
    }
}

impl From<[u8; 256]> for Password {
    fn from(dat: [u8; 256]) -> Password {
        Password(dat)
    }
}

impl Password {
    pub fn new() -> Password {
        let mut arr = [0u8; 256];
        for i in 0..256 {
            arr[i] = i as u8;
        }
        arr[..].shuffle(&mut rand::thread_rng());
        Password(arr)
    }

    pub fn valid(&self) -> bool {
        let mut table = [0u8; 256];
        for i in self.0.iter() {
            table[*i as usize] = *i;
        }
        for (i, v) in table.iter().enumerate() {
            if i as u8 != *v {
                return false;
            }
        }
        true
    }
}

#[test]
fn password() {
    let p = Password::new();
    let mut set = std::collections::HashSet::new();
    for b in p.0.iter() {
        if set.contains(b) {
            panic!("{}", b);
        } else {
            set.insert(b);
        }
    }
    let s = p.to_string();
    let passwd = Password::from(s);
    assert_eq!(true, passwd.valid());
    for (&a, &b) in passwd.0.iter().zip(p.0.iter()) {
        assert_eq!(a, b);
    }
}

pub struct Cipher {
    encoder: Password,
    decoder: Password,
}

impl Cipher {
    pub fn new(encoder: Password) -> Cipher {
        let mut decoder = Password([0u8; 256]);
        for (i, &v) in encoder.0.iter().enumerate() {
            decoder.0[v as usize] = i as u8;
        }
        Cipher {
            encoder: encoder,
            decoder: decoder,
        }
    }
    pub fn encode(&self, dat: &mut Vec<u8>) {
        for v in dat {
            *v = self.encoder.0[*v as usize];
        }
    }
    pub fn decode(&self, dat: &mut Vec<u8>) {
        for v in dat {
            *v = self.decoder.0[*v as usize];
        }
    }
}
