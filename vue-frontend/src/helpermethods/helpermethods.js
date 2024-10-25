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

export async function getPolls() {
  try {
    const response = await fetch('http://localhost:8080/api/polls', {
      method: 'GET',
    })

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`)
    }

    //No content
    if (response.status == 204) {
      return []
    }

    return await response.json()
  } catch (error) {
    console.error('Failed to fetch polls', error)
    return []
  }
}

export async function getVoteOptions() {
  try {
    const response = await fetch('http://localhost:8080/api/voteoptions', {
      method: 'GET',
    })

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`)
    }

    //No content
    if (response.status === 204) {
      return []
    }

    return await response.json()
  } catch (error) {
    console.error('Failed to fetch polls', error)
    return []
  }
}

export async function deletePoll(pollId) {
  try {
    const response = await fetch(`http://localhost:8080/api/polls/${pollId}`, {
      method: 'DELETE',
    })

    if (response.status === 404) {
    throw new Error(`Pole with ID ${pollId} not found`);
    }

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    console.log(`Poll with ID ${pollId} deleted successfully`, response)

  } catch (error) {
    console.error(`Failed to delete poll with ID ${pollId}`, error);
    throw error;
  }
}
