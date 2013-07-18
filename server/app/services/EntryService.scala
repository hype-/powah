package services

import models.Entry
import repositories.EntryRepository
import com.google.inject.Inject

class EntryService @Inject()(entryRepository: EntryRepository) {
  def getEntries: List[Entry] = entryRepository.findAll

  def addEntry(name: String): Entry = entryRepository.addEntry(name)
}
