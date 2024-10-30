export function getGuestId(){
 let guestId = localStorage.getItem('guestId');
  if(!guestId){
    guestId = generateUUID();
    localStorage.setItem('guestId', guestId);
  }
  return guestId;
}

export function generateUUID(){
  return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c){
    let r = Math.random()*16|0,
      v = c == 'x' ? r : (r&0x3|0x8);
    return v.toString(16);
  });
}

export function isLoggedIn(){
  return localStorage.getItem('guestId') === null;
}


