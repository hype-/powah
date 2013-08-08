package powah.entry

import com.google.inject.Inject
import powah.user.User

class EntryService @Inject()(entryRepository: EntryRepository) {
  def getEntries: List[EntryOutput] = entryRepository.findAll.map(_.toOutput)

  def addEntry(name: String, user: User): Entry =
    entryRepository.add(name, user)
}
