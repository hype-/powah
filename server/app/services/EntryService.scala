package services

import models.{EntryOutput, Entry, User}
import repositories.EntryRepository
import com.google.inject.Inject

class EntryService @Inject()(entryRepository: EntryRepository) {
  def getEntries: List[EntryOutput] = entryRepository.findAll.map(_.toOutput)

  def addEntry(name: String, user: User): Entry =
    entryRepository.addEntry(name, user)
}
