export function createNewUser(username, password, email) {
  fetch('http://localhost:8080/api/users', {
    method: 'POST',
    body: JSON.stringify({
      username: username,
      password: password,
      email: email,
    }),
    headers: {
      'Content-Type': 'application/json',
    },
  })
    .then(response => {
      if (response.ok) {
        username = ''
        password = ''
        email = ''
        console.log('user created successfully')
      } else {
        return response.json().then(data => {
          throw new Error(data.message || 'Failed to create user')
        })
      }
    })
    .catch(error => {
      alert(error.message)
    })
}

export function createNewPoll(question, hoursvalid, voteoptions) {
  const now = new Date()
  const validUntil = new Date(now)
  validUntil.setHours(now.getHours() + hoursvalid)
  fetch('http://localhost:8080/api/polls', {
    method: 'POST',
    body: JSON.stringify({
      question: question,
      publishedAt: now.getTime(),
      validUntil: validUntil.getTime(),
      voteOptions: voteoptions,
    }),
    headers: {
      'Content-Type': 'application/json',
    },
  })
    .then(response => {
      if (response.ok) {
        question = ''
        hoursvalid = 0
        voteoptions = []
        console.log('poll created successfully')
      } else {
        return response.json().then(data => {
          throw new Error(data.message || 'Failed to create poll')
        })
      }
    })
    .catch(error => {
      alert(error.message)
    })
}
